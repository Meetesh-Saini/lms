import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { NotificationService } from './notification.service';
import { CallerHandledException, RequestHandledException } from './exceptions';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiBase = 'http://localhost:8080';
  private apiUrl = `${this.apiBase}/api`;

  constructor(private http: HttpClient, private notification: NotificationService, private router: Router) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt');
    return new HttpHeaders({
      Authorization: token ? `Bearer ${token}` : ''
    });
  }

  private handleResponse<T>(response: any, isError: boolean): T {
    if (response.success === false && response.data === "jwt-error") {
      localStorage.removeItem("jwt");
      this.router.navigate(['/auth']);
      this.notification.show(response.message, true);
    }
    if (response.success === false) {
      // Error handled by the caller
      // debugger
      throw new CallerHandledException(response);
    }

    if (response.type && response.title) {
      this.notification.show(response.title, true);
      throw new RequestHandledException(response.detail || response.title);
    }

    if (isError) {
      // debugger
      this.notification.show('An error occurred', true);
      throw new RequestHandledException('No response or unexpected error');
    }

    return response as T;
  }

  // Handle errors
  private handleError(error: HttpErrorResponse): Observable<never> {
    if (error.error && typeof error.error === 'object') {
      // Specific error object
      this.handleResponse(error.error, true);
    } else {
      // Fallback error handling
      this.notification.show('An error occurred', true);
    }
    return throwError(() => error);
  }

  // GET Request
  get<T>(url: string): Observable<T> {
    return this.http
      .get<T>(`${this.apiUrl}${url}`, { headers: this.getAuthHeaders() })
      .pipe(catchError((error) => this.handleError(error)));
  }

  // POST Request
  post<T>(url: string, body: any): Observable<T> {
    return this.http
      .post<T>(`${this.apiUrl}${url}`, body, { headers: this.getAuthHeaders() })
      .pipe(catchError((error) => this.handleError(error)));
  }

  // PUT Request
  put<T>(url: string, body: any): Observable<T> {
    return this.http
      .put<T>(`${this.apiUrl}${url}`, body, { headers: this.getAuthHeaders() })
      .pipe(catchError((error) => this.handleError(error)));
  }

  // DELETE Request
  delete<T>(url: string): Observable<T> {
    return this.http
      .delete<T>(`${this.apiUrl}${url}`, { headers: this.getAuthHeaders() })
      .pipe(catchError((error) => this.handleError(error)));
  }

  api(base = false) {
    if (base) return this.apiBase;
    return this.apiUrl;
  }

  addContent(formData: FormData): Observable<any> {
    return this.http.post<any>(this.apiUrl + "/contents", formData, { headers: this.getAuthHeaders() });
  }

  getWithOptions(url: string, extraOptions: any, useBase = false): Observable<any> {
    return this.http
      .get(`${useBase ? this.apiBase : this.apiUrl}${url}`, { headers: this.getAuthHeaders(), ...extraOptions })
  }

  getUsername(): string | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const payload = token.split('.')[1];
      const decodedPayload = atob(payload);
      const body = JSON.parse(decodedPayload);
      return body?.sub;
    }
    return null;
  }
}

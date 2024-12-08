import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../api.service';
import { Router } from '@angular/router';
import { NotificationService } from '../notification.service';
import { CallerHandledException, RequestHandledException } from '../exceptions';



@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [MatButtonModule, MatCardModule, MatInputModule, MatTabsModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
})
export class AuthComponent {

  selectedTabIndex: number = 0;

  loginData = { username: '', password: '' };
  registerData = { username: '', email: '', phone: '', password: '' };

  constructor(private apiService: ApiService, private router: Router, private notification: NotificationService) { }

  isLogin() {
    return this.selectedTabIndex == 0;
  }

  onSubmitLogin() {
    this.apiService.post('/auth/login', this.loginData).subscribe({
      next: (response: any) => {
        if (response.success) {
          localStorage.setItem('jwt', response.data.token);
          this.router.navigate(['/']);  // Redirect to home page
        }
      },
      error: (error) => {
        // debugger
        if (error instanceof CallerHandledException) {
          this.notification.show(error.resource.message, true);
        }
        else if (!(error instanceof RequestHandledException)) {
          this.notification.show('An error occurred during login', true);
        }
      }
    });
  }


  onSubmitRegister() {
    this.apiService.post<any>('/auth/register', this.registerData).subscribe({
      next: (response) => {
        if (response.success) {
          this.notification.show('Registration successful!', false);
        }
      },
      error: (error) => {
        if (!(error instanceof RequestHandledException)) {
          this.notification.show('An error occurred during registration', true);
        }
      }
    });
  }
}

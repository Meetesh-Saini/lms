import { Component, OnInit } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    MatDialogModule,
    NavbarComponent,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  username: string | null = '';

  constructor(private router: Router) { }

  ngOnInit(): void {
    this.username = this.getUsername() ?? "User";
  }

  private getUsername(): string | null {
    const token = localStorage.getItem('jwt');
    if (token) {
      const payload = token.split('.')[1];
      const decodedPayload = atob(payload);
      const body = JSON.parse(decodedPayload);
      return body?.sub;
    }
    return null;
  }

  exploreCourses(): void {
    this.router.navigate(['/courses'])
  }
}

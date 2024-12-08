import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, RouterModule],
})
export class NavbarComponent {
  isLoggedIn = false;

  constructor(private router: Router) { }

  ngOnInit() {
    this.isLoggedIn = localStorage.getItem("jwt") !== null;
    if (!this.isLoggedIn) {
      this.logout();
    }
  }

  toggleLoginLogout() {
    this.isLoggedIn = !this.isLoggedIn;
    if (this.isLoggedIn) {
      this.router.navigate(['/']);
    } else {
      this.logout();
    }
  }

  logout() {
    localStorage.removeItem("jwt");
    this.router.navigate(['/auth']);
  }
}

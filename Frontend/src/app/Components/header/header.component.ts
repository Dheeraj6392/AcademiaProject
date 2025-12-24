import { Component, HostListener } from '@angular/core';
import { AuthService } from '../../Services/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  currentTime: string = '';
  ngOnInit(): void {
    setInterval(() => {
      const now = new Date();
      this.currentTime = now.toLocaleTimeString();
    }, 1000);
  }
  constructor(public auth: AuthService) {}
  showProfileMenu: boolean = false;

  get username() {
    return localStorage.getItem('username');
  }
  // Add initials getter for profile avatar
  get initials() {
    const u = this.username || '';
    return u ? u.charAt(0).toUpperCase() : '?';
  }
  // Toggle profile dropdown menu
  toggleProfileMenu() {
    this.showProfileMenu = !this.showProfileMenu;
  }
  // Close profile menu when clicking anywhere in the document
  @HostListener('document:click', ['$event'])
  onDocumentClick() {
    this.showProfileMenu = false;
  }

  logout() {
    this.auth.logout();
  }
}

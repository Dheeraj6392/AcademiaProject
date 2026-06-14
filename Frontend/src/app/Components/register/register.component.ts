import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  constructor(private router: Router) {
    // New backend auto-creates account on first login — no separate registration needed
    this.router.navigateByUrl('/login');
  }
}
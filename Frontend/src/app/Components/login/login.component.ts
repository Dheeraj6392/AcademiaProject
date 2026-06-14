import { Component } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { AuthService } from '../../Services/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  error = '';
  isNewUser = false;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.pattern(/.*@iiita\.ac\.in$/)]],
      name:  ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  submit() {
    this.error = '';
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;
    const { email, name } = this.form.value;
    const call$ = this.isNewUser
      ? this.auth.register(email, name)
      : this.auth.login(email, name);

    call$.subscribe({
      next: () => { this.loading = false; this.router.navigateByUrl('/'); },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || (this.isNewUser ? 'Registration failed' : 'Login failed');
      }
    });
  }
}

import { Component } from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
})
export class LoginComponent {
  error: string = '';

  // Declare loginForm variable
  loginForm;

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    // Initialize loginForm in the constructor
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    console.log('Form submitted');
    if (this.loginForm.valid) {
      const formValues = this.loginForm.value;

      const loginData = {
        username: formValues.username ?? '',
        password: formValues.password ?? '',
      };

      this.auth.login(loginData).subscribe({
        next: (res) => {
          const role = res.role;
          this.auth.saveToken(res.token);

          switch (role) {
            case 'ADMIN':
              console.log('Redirecting to admin dashboard');
              this.router.navigate(['/admin-dashboard']);
              break;
            case 'CLIENT':
              console.log('Redirecting to client dashboard');
              this.router.navigate(['/client-dashboard']);
              break;
            case 'EMPLOYEE':
              console.log('Redirecting to employee dashboard');
              this.router.navigate(['/employee-dashboard']);
              break;
            default:
              console.log('Role inconnu');
              this.error = 'Rôle inconnu';
          }
        },
        error: (err) => {
          this.error = 'Échec de la connexion';
          console.error('Error during login', err);
        },
      });
    }
  }

}

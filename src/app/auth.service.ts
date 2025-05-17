import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, catchError, Observable, tap, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/login2_war_exploded/api/auth/login';
  private tokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<any> {

    const headers = { 'Content-Type': 'application/json' };

    return this.http.post(this.apiUrl, credentials, { headers });
  }

  // login(credentials: { username: string; password: string }): Observable<any> {
  //   const apiUrl = 'http://localhost:8080/secure_login_war_exploded/api/auth/login';
  //   const headers = { 'Content-Type': 'application/json' };
  //
  //   return this.http.post(apiUrl, credentials, { headers }).pipe(
  //     tap((res) => console.log('Login response:', res)),
  //     catchError((error) => {
  //       console.error('Login error:', error);
  //       return throwError(() => error);
  //     })
  //   );
  // }


  saveToken(token: string): void {
    localStorage.setItem('authToken', token);
    this.tokenSubject.next(token);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getRoleFromToken(token: string): string {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role;
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this.tokenSubject.next(null);
  }
}

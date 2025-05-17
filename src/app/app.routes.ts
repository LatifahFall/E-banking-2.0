import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ClientDashboardComponent } from './client-dashboard/client-dashboard.component';
import { EmployeeDashboardComponent } from './employee-dashboard/employee-dashboard.component';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./login/login.component').then(m => m.LoginComponent) },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {path: 'admin-dashboard',
    loadComponent: () => import('./admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  {path: 'client-dashboard',
    loadComponent: () => import('./client-dashboard/client-dashboard.component').then(m => m.ClientDashboardComponent)
  },
  {path: 'employee-dashboard',
    loadComponent: () => import('./employee-dashboard/employee-dashboard.component').then(m => m.EmployeeDashboardComponent)
  }
];


import { Component } from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
  standalone: true,
})
export class AdminDashboardComponent {
  constructor(private route: ActivatedRoute) {
    console.log('Navigated to Admin Dashboard!');
    this.route.params.subscribe(params => {
      console.log('Route params:', params);
    });
  }
}

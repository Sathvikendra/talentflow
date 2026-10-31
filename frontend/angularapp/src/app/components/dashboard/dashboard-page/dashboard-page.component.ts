import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardService } from '../../../services/dashboard.service';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements OnInit {

  metrics: any;

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {
    this.dashboardService
      .getMetrics()
      .subscribe(data => {
        this.metrics = data;
      });
  }
}
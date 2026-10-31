import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  standalone: false,
  styleUrl: './status-badge.component.scss',
  templateUrl: './status-badge.component.html',
})
export class StatusBadge {
  @Input() status = '';

  get displayStatus(): string {
    return this.status
      .toLowerCase()
      .split('_')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  get statusClass(): string {
    const status = this.status.toUpperCase();

    if (['OPEN', 'L1_CLEARED', 'CI_ACCEPTED', 'CLEARED', 'ACCEPTED'].includes(status)) {
      return 'status-success';
    }

    if (['L1_REJECTED', 'CI_REJECTED', 'REJECTED', 'CLOSED'].includes(status)) {
      return 'status-danger';
    }

    if (['L1_SCHEDULED', 'CI_SCHEDULED', 'L1_PENDING', 'CI_PENDING'].includes(status)) {
      return 'status-progress';
    }

    if (['WITHDRAWN', 'INACTIVE'].includes(status)) {
      return 'status-neutral';
    }

    if (['PROFILE_RECEIVED', 'FILLED'].includes(status)) {
      return 'status-info';
    }

    return 'status-neutral';
  }
}

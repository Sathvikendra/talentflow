import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-metric-card',
  standalone: false,
  styleUrl: './metric-card.component.scss',
  templateUrl: './metric-card.component.html',
})
export class MetricCard {
  @Input() title = '';
  @Input() value: number | string = 0;
  @Input() description = '';
  @Input() icon = '•';
}

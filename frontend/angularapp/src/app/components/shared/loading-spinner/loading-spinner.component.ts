import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-spinner',
  standalone: false,
  styleUrl: './loading-spinner.component.scss',
  templateUrl: './loading-spinner.component.html',
})
export class LoadingSpinner {
  @Input() message = 'Loading...';
}

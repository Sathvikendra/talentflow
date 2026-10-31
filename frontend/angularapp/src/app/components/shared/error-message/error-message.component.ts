import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-message',
  standalone: false,
  styleUrl: './error-message.component.scss',
  templateUrl: './error-message.component.html',
})
export class ErrorMessage {
  @Input() title = 'Something went wrong';
  @Input() message = 'We could not complete your request.';
  @Input() retryLabel = '';

  @Output() retryRequested = new EventEmitter<void>();

  retry(): void {
    this.retryRequested.emit();
  }
}

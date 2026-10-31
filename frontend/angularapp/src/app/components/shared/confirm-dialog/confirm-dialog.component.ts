import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-dialog',
  standalone: false,
  styleUrl: './confirm-dialog.component.scss',
  templateUrl: './confirm-dialog.component.html',
})
export class ConfirmDialog {
  @Input() visible = false;
  @Input() title = 'Are you sure?';
  @Input() message = 'Please confirm that you want to continue.';
  @Input() confirmLabel = 'Confirm';
  @Input() cancelLabel = 'Cancel';
  @Input() destructive = false;
  @Input() busy = false;

  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  confirm(): void {
    if (this.visible && !this.busy) {
      this.confirmed.emit();
    }
  }

  cancel(): void {
    if (this.busy) {
      return;
    }

    this.cancelled.emit();
  }
}

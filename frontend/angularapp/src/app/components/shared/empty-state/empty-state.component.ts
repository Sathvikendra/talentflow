import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: false,
  styleUrl: './empty-state.component.scss',
  templateUrl: './empty-state.component.html',
})
export class EmptyState {
  @Input() title = 'Nothing here yet';
  @Input() description = 'There are no items to display.';
  @Input() actionLabel = '';

  @Output() actionClicked = new EventEmitter<void>();

  onActionClick(): void {
    this.actionClicked.emit();
  }
}

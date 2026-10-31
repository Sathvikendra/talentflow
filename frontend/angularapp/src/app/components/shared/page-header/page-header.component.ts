import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-page-header',
  standalone: false,
  styleUrl: './page-header.component.scss',
  templateUrl: './page-header.component.html',
})
export class PageHeader {
  @Input() title = '';
  @Input() description = '';
  @Input() actionLabel = '';
  @Input() actionDisabled = false;

  @Output() actionClicked = new EventEmitter<void>();

  onActionClick(): void {
    if (!this.actionDisabled) {
      this.actionClicked.emit();
    }
  }
}

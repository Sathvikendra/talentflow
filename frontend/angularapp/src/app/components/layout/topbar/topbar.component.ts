import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-topbar',
  standalone: false,
  styleUrl: './topbar.component.scss',
  templateUrl: './topbar.component.html',
})
export class Topbar {
  @Input() sidebarCollapsed = false;
  @Input() userName = 'User';

  @Output() menuToggle = new EventEmitter<void>();
  @Output() logoutRequested = new EventEmitter<void>();

  accountMenuOpen = false;

  toggleAccountMenu(): void {
    this.accountMenuOpen = !this.accountMenuOpen;
  }

  closeAccountMenu(): void {
    this.accountMenuOpen = false;
  }

  requestLogout(): void {
    this.closeAccountMenu();
    this.logoutRequested.emit();
  }
}

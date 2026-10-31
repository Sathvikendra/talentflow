import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-app-shell',
  standalone: false,
  styleUrl: './app-shell.component.scss',
  templateUrl: './app-shell.component.html',
})
export class AppShell {
  sidebarCollapsed = false;

  @Output() logoutRequested = new EventEmitter<void>();

  toggleSidebar(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  requestLogout(): void {
    this.logoutRequested.emit();
  }
}

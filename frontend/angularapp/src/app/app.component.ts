import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: false,
  styleUrl: './app.component.scss',
  templateUrl: './app.component.html',
})
export class App {
  protected readonly title = signal('angularapp');
  // constructor(private authService: AuthService) {
  //   console.log('TalentFlow API URL:', this.authService.getApiUrl());
  // }
}

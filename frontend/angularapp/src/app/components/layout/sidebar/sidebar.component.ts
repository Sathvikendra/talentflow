import { Component, Input } from '@angular/core';

interface NavigationItem {
  label: string;
  route: string;
  icon: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: false,
  styleUrl: './sidebar.component.scss',
  templateUrl: './sidebar.component.html',
})
export class Sidebar {
  @Input() collapsed = false;

  readonly navigationItems: NavigationItem[] = [
    { label: 'Dashboard', route: '/home', icon: '▦' },
    { label: 'Requirements', route: '/requirements', icon: '▤' },
    { label: 'Candidates', route: '/candidates', icon: '♙' },
    { label: 'Submissions', route: '/submissions', icon: '⇄' },
    { label: 'L1 Evaluations', route: '/l1-evaluations', icon: '✓' },
    { label: 'Client Interviews', route: '/client-interviews', icon: '▣' },
    { label: 'AI Assessments', route: '/ai-assessments', icon: '✳' },
    { label: 'Notifications', route: '/notifications', icon: '♧' },
    { label: 'Administration', route: '/admin', icon: '⚙' }
  ];
}

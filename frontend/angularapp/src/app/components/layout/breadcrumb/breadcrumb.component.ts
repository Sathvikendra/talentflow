import { Component } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, Subscription } from 'rxjs';

interface BreadcrumbItem {
  label: string;
  url: string;
}

@Component({
  selector: 'app-breadcrumb',
  standalone: false,
  styleUrl: './breadcrumb.component.scss',
  templateUrl: './breadcrumb.component.html',
})
export class Breadcrumb {
  breadcrumbs: BreadcrumbItem[] = [];

  private routerSubscription?: Subscription;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.updateBreadcrumbs();

    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.updateBreadcrumbs());
  }

  ngOnDestroy(): void {
    this.routerSubscription?.unsubscribe();
  }

  private updateBreadcrumbs(): void {
    const items: BreadcrumbItem[] = [];
    let route = this.activatedRoute.root;
    let url = '';

    while (route.firstChild) {
      route = route.firstChild;

      const routeSegment = route.snapshot.url
        .map(segment => segment.path)
        .join('/');

      if (routeSegment) {
        url += `/${routeSegment}`;
      }

      const label = route.snapshot.data['breadcrumb'] as string | undefined;

      if (label && label.toLowerCase() !== 'login') {
        items.push({
          label,
          url: url || '/'
        });
      }
    }

    this.breadcrumbs = items;
  }
}

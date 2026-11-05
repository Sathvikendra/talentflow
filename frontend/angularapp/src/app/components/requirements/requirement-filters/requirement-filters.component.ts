import { Component, EventEmitter, Output } from '@angular/core';

export interface RequirementFilterValues {
  search: string;
  status: string;
  priority: string;
  workMode: string;
}

@Component({
  selector: 'app-requirement-filters',
  standalone: false,
  styleUrl: './requirement-filters.component.scss',
  templateUrl: './requirement-filters.component.html',
})
export class RequirementFilters {
  @Output() filtersChanged = new EventEmitter<RequirementFilterValues>();

  filters: RequirementFilterValues = {
    search: '',
    status: '',
    priority: '',
    workMode: ''
  };

  updateFilter(
    field: keyof RequirementFilterValues,
    value: string
  ): void {
    this.filters[field] = value;
    this.emitFilters();
  }

  emitFilters(): void {
    this.filtersChanged.emit({ ...this.filters });
  }

  clearFilters(): void {
    this.filters = {
      search: '',
      status: '',
      priority: '',
      workMode: ''
    };

    this.emitFilters();
  }
}

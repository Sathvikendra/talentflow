export type RequirementStatus = 'OPEN' | 'FILLED' | 'CLOSED';

export interface RequirementClient {
  id: number;
  clientCode?: string;
  clientName?: string;
  industry?: string | null;
}

export interface RequirementUser {
  id: number;
  username?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  role?: string;
}

export interface Requirement {
  id: number;
  rrNumber: string;
  client: RequirementClient | null;
  title: string;
  description: string;
  employmentType: string | null;
  location: string | null;
  workMode: string;
  onshoreOrOffshore: string;
  minExperienceYears: number | null;
  maxExperienceYears: number | null;
  positionsCount: number;
  priority: string | null;
  status: RequirementStatus;
  createdBy: RequirementUser | null;
  openedAt: string | null;
  targetFillDate: string | null;
  closedAt: string | null;
  createdAt: string | null;
  updatedAt: string | null;
}

export interface CreateRequirementRequest {
  rrNumber: string;
  clientId: number;
  title: string;
  description: string;
  employmentType: string | null;
  location: string | null;
  workMode: string;
  onshoreOrOffshore: string;
  minExperienceYears: number | null;
  maxExperienceYears: number | null;
  positionsCount: number;
  priority: string | null;
  createdBy: number;
  targetFillDate: string | null;
}
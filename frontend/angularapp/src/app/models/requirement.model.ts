export interface Requirement {
  id: number;
  rrNumber: string;
  client: RequirementClient | null;
  title: string;
  description: string;
  employmentType: string | null;
  location: string | null;
  workMode: string | null;
  onshoreOrOffshore: string | null;
  minExperienceYears: number | null;
  maxExperienceYears: number | null;
  positionsCount: number | null;
  priority: string | null;
  status: RequirementStatus;
  createdBy: RequirementUser | null;
  openedAt: string | null;
  targetFillDate: string | null;
  closedAt: string | null;
  createdAt: string | null;
  updatedAt: string | null;
}

export interface RequirementClient {
  id: number;
  name?: string;
}

export interface RequirementUser {
  id: number;
  username?: string;
  email?: string;
}

export interface RequirementResponse {
  id: number;
  rrNumber: string;
  title: string;
  status: RequirementStatus;
  clientName: string;
  createdBy: string;
}

export interface CreateRequirementRequest {
  rrNumber: string;
  clientId: number;
  title: string;
  description: string;
  employmentType?: string | null;
  location?: string | null;
  workMode: string;
  onshoreOrOffshore: string;
  minExperienceYears?: number | null;
  maxExperienceYears?: number | null;
  positionsCount: number;
  priority?: string | null;
  createdBy: number;
  targetFillDate?: string | null;
}

export interface UpdateRequirementRequest {
  title?: string;
  description?: string;
  employmentType?: string | null;
  location?: string | null;
  workMode?: string;
  onshoreOrOffshore?: string;
  minExperienceYears?: number | null;
  maxExperienceYears?: number | null;
  positionsCount?: number;
  priority?: string | null;
  status?: RequirementStatus;
  targetFillDate?: string | null;
}

export type RequirementStatus = 'OPEN' | 'FILLED' | 'CLOSED';
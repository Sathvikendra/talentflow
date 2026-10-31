export type SubmissionStatus =
  | 'PROFILE_RECEIVED'
  | 'L1_PENDING'
  | 'L1_SCHEDULED'
  | 'L1_CLEARED'
  | 'L1_REJECTED'
  | 'CI_PENDING'
  | 'CI_SCHEDULED'
  | 'CI_ACCEPTED'
  | 'CI_REJECTED'
  | 'WITHDRAWN';

export interface Submission {
  id: number;
  candidateId: number;
  requirementId: number;
  currentStatus: SubmissionStatus;
  submittedBy: number;
  assignedTo: number | null;
  submissionNotes: string | null;
  assignedAt: string | null;
  createdAt: string | null;
  updatedAt: string | null;
}

export interface CreateSubmissionRequest {
  candidateId: number;
  requirementId: number;
  submissionNotes?: string | null;
}

export interface UpdateSubmissionRequest {
  currentStatus?: SubmissionStatus;
  assignedTo?: number | null;
  submissionNotes?: string | null;
}

export interface UpdateSubmissionStatusRequest {
  status: SubmissionStatus;
  reason?: string | null;
  comments?: string | null;
}

export interface AssignSubmissionRequest {
  assignedTo: number;
}
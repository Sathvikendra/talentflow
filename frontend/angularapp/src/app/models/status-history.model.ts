import { SubmissionStatus } from './submission.model';

export interface StatusHistory {
  id: number;
  submissionId: number;
  oldStatus: SubmissionStatus | null;
  newStatus: SubmissionStatus;
  changedBy: number;
  transitionReason: string | null;
  comments: string | null;
  changedAt: string | null;
}
import { SubmissionStatus } from './submission.model';

export interface StatusHistory {
  id: number;
  submissionId: number;
  oldStatus: SubmissionStatus | null;
  newStatus: SubmissionStatus;
  changedBy: number | null;
  transitionReason: string | null;
  comments: string | null;
  changedAt: string;
}
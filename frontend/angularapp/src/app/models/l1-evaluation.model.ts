export type L1EvaluationResult = 'CLEARED' | 'REJECTED';

export interface L1Evaluation {
  id: number;
  submissionId: number;
  interviewerId: number;
  scheduledAt: string | null;
  durationMinutes: number | null;
  meetingLink: string | null;
  conductedAt: string | null;
  result: L1EvaluationResult | null;
  technicalComments: string | null;
  overallComments: string | null;
  recommendation: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateL1EvaluationRequest {
  submissionId: number;
  interviewerId: number;
  scheduledAt?: string | null;
  durationMinutes?: number | null;
  meetingLink?: string | null;
}

export interface ScheduleL1EvaluationRequest {
  scheduledAt: string;
  durationMinutes?: number | null;
  meetingLink?: string | null;
}

export interface UpdateL1EvaluationRequest {
  scheduledAt?: string | null;
  durationMinutes?: number | null;
  meetingLink?: string | null;
  conductedAt?: string | null;
  technicalComments?: string | null;
  overallComments?: string | null;
  recommendation?: string | null;
}

export interface RecordL1ResultRequest {
  result: L1EvaluationResult;
  conductedAt?: string | null;
  technicalComments?: string | null;
  overallComments?: string | null;
  recommendation?: string | null;
}
export type ClientInterviewResult = 'ACCEPTED' | 'REJECTED';

export interface ClientInterview {
  id: number;
  submissionId: number;
  clientInterviewerName: string | null;
  clientInterviewerEmail: string | null;
  scheduledAt: string | null;
  durationMinutes: number | null;
  meetingLink: string | null;
  conductedAt: string | null;
  result: ClientInterviewResult | null;
  comments: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateClientInterviewRequest {
  submissionId: number;
  clientInterviewerName?: string | null;
  clientInterviewerEmail?: string | null;
  scheduledAt?: string | null;
  durationMinutes?: number | null;
  meetingLink?: string | null;
}

export interface ScheduleClientInterviewRequest {
  scheduledAt: string;
  durationMinutes?: number | null;
  meetingLink?: string | null;
}

export interface UpdateClientInterviewRequest {
  clientInterviewerName?: string | null;
  clientInterviewerEmail?: string | null;
  scheduledAt?: string | null;
  durationMinutes?: number | null;
  meetingLink?: string | null;
  comments?: string | null;
}

export interface RecordClientInterviewResultRequest {
  result: ClientInterviewResult;
  conductedAt?: string | null;
  comments?: string | null;
}
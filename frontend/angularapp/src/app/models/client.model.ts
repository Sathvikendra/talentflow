
export interface Client {
  id: number;
  clientCode: string;
  clientName: string;
  industry: string | null;
  contactName: string | null;
  contactEmail: string | null;
  contactPhone: string | null;
  notes: string | null;
  isActive: boolean;
  createdAt: string | null;
  updatedAt: string | null;
}

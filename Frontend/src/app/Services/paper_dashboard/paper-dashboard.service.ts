import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Paper {
  id: string;
  title: string;
  subject: string;
  branch: string;
  year: number;
  examType: string;
  fileUrl: string;
  downloadCount: number;
  uploadedBy: string;
  createdAt: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

@Injectable({ providedIn: 'root' })
export class PaperDashboardService {
  private base = 'http://localhost:8080/api/v1/papers';

  constructor(private http: HttpClient) {}

  getPapers(filters: {
    q?: string;
    branch?: string;
    subject?: string;
    year?: number;
    examType?: string;
    page?: number;
    size?: number;
  }): Observable<PageResponse<Paper>> {
    let params = new HttpParams();
    if (filters.q)        params = params.set('q', filters.q);
    if (filters.branch)   params = params.set('branch', filters.branch);
    if (filters.subject)  params = params.set('subject', filters.subject);
    if (filters.year)     params = params.set('year', filters.year.toString());
    if (filters.examType) params = params.set('examType', filters.examType);
    params = params.set('page', (filters.page ?? 0).toString());
    params = params.set('size', (filters.size ?? 10).toString());
    return this.http.get<PageResponse<Paper>>(this.base, { params });
  }

  getDownloadUrl(id: string): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${this.base}/${id}/download`);
  }

  deletePaper(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  uploadPaper(formData: FormData): Observable<Paper> {
    return this.http.post<Paper>(this.base, formData);
  }
}

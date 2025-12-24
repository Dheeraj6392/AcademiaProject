import { Component, OnInit } from '@angular/core';
import { PaperModerationService, UploadedFile } from '../../Services/paper_moderation/paper-moderation.service';

@Component({
  selector: 'app-admin-papers',
  templateUrl: './admin-papers.component.html',
  styleUrls: ['./admin-papers.component.css']
})
export class AdminPapersComponent implements OnInit {
  pending: UploadedFile[] = [];
  loading = false;
  error = '';

  constructor(private moderation: PaperModerationService) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending() {
    this.loading = true;
    this.error = '';
    this.moderation.getPending().subscribe({
      next: (files) => {
        this.pending = files;
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.error || 'Failed to load pending files';
        this.loading = false;
      }
    });
  }

  approve(id: number) {
    this.loading = true;
    this.moderation.approve(id).subscribe({
      next: () => this.loadPending(),
      error: (err) => {
        this.error = err?.error?.error || 'Failed to approve file';
        this.loading = false;
      }
    });
  }

  reject(id: number) {
    this.loading = true;
    this.moderation.reject(id).subscribe({
      next: () => this.loadPending(),
      error: (err) => {
        this.error = err?.error?.error || 'Failed to reject file';
        this.loading = false;
      }
    });
  }
}
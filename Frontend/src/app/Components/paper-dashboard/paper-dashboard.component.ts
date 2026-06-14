import { Component, OnInit } from '@angular/core';
import { Paper, PaperDashboardService, PageResponse } from '../../Services/paper_dashboard/paper-dashboard.service';

@Component({
  selector: 'app-paper-dashboard',
  templateUrl: './paper-dashboard.component.html',
  styleUrls: ['./paper-dashboard.component.css']
})
export class PaperDashboardComponent implements OnInit {

  papers: Paper[] = [];
  loading = false;
  error = '';

  // filters
  searchQuery = '';
  selectedBranch = '';
  selectedExamType = '';
  selectedYear: number | undefined;

  // pagination
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  branches = ['ECE', 'CS', 'IT', 'ME', 'CE'];
  examTypes = ['MID_SEM', 'END_SEM'];

  constructor(private paperService: PaperDashboardService) {}

  ngOnInit() { this.loadPapers(); }

  loadPapers() {
    this.loading = true;
    this.error = '';
    this.paperService.getPapers({
      q:        this.searchQuery || undefined,
      branch:   this.selectedBranch || undefined,
      examType: this.selectedExamType || undefined,
      year:     this.selectedYear,
      page:     this.currentPage,
      size:     this.pageSize
    }).subscribe({
      next: (res: PageResponse<Paper>) => {
        this.papers = res.content;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.loading = false;
      },
      error: () => { this.error = 'Failed to load papers'; this.loading = false; }
    });
  }

  search() { this.currentPage = 0; this.loadPapers(); }

  resetFilters() {
    this.searchQuery = '';
    this.selectedBranch = '';
    this.selectedExamType = '';
    this.selectedYear = undefined;
    this.currentPage = 0;
    this.loadPapers();
  }

  download(paper: Paper) {
    this.paperService.getDownloadUrl(paper.id).subscribe({
      next: (res) => window.open(res.url, '_blank'),
      error: () => alert('Login required to download')
    });
  }

  goToPage(page: number) {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadPapers();
  }

  pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  formatExamType(et: string) {
    return et === 'MID_SEM' ? 'Mid Sem' : 'End Sem';
  }
}

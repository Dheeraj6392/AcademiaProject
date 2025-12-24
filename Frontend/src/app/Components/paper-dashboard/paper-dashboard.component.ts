import { Component, Input, OnInit } from '@angular/core';
import { PaperDashboardService, QPaper } from '../../Services/paper_dashboard/paper-dashboard.service';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-paper-dashboard',
  templateUrl: './paper-dashboard.component.html',
  styleUrls: ['./paper-dashboard.component.css']
})
export class PaperDashboardComponent implements OnInit {
  @Input() course!: string;
  pdfUrl!: SafeResourceUrl;
  qpapers: QPaper[] = [];
  filteredQpapers: QPaper[] = [];
  searchTerm: string = '';
  selectedImage: string | null = null;

  constructor(private qpaperService: PaperDashboardService, private route: ActivatedRoute, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      this.course = params['course'] || '';
    });
    this.qpaperService.getQpapers().subscribe(data => {
      this.qpapers = data;
    });
  }

  onSearch(): void {
    const searchYear = this.searchTerm?.trim();

    this.filteredQpapers = this.qpapers.filter(paper => {
      const paperYear = paper.year?.replace(/"/g, '');
      return !searchYear || paperYear === searchYear;
    });
  }

  selectedPdfUrl: SafeResourceUrl | null = null;
  openModal(url: string): void {
    this.selectedPdfUrl =
      this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  closeModal(): void {
    this.selectedPdfUrl = null;
  }
}

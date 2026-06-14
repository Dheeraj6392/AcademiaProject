import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Paper, PaperDashboardService, PageResponse } from '../../Services/paper_dashboard/paper-dashboard.service';
import { CourseService, CourseResponse } from '../../Services/course/course.service';

interface User {
  id: string;
  email: string;
  name: string;
  role: string;
}

@Component({
  selector: 'app-admin-papers',
  templateUrl: './admin-papers.component.html',
  styleUrls: ['./admin-papers.component.css']
})
export class AdminPapersComponent implements OnInit {

  activeTab: 'papers' | 'users' | 'courses' = 'papers';

  // Papers tab
  papers: Paper[] = [];
  papersLoading = false;
  papersError = '';
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  // Users tab
  users: User[] = [];
  usersLoading = false;
  usersError = '';

  // Courses tab
  courses: CourseResponse[] = [];
  coursesLoading = false;
  coursesError = '';
  courseForm: FormGroup;
  creatingCourse = false;

  constructor(
    private paperService: PaperDashboardService,
    private courseService: CourseService,
    private http: HttpClient,
    private fb: FormBuilder
  ) {
    this.courseForm = this.fb.group({
      title:       ['', Validators.required],
      description: [''],
      weeks:       [4,  [Validators.required, Validators.min(1)]],
      lessons:     [10, [Validators.required, Validators.min(1)]],
      price:       [0,  [Validators.required, Validators.min(0)]],
      imageUrl:    ['']
    });
  }

  ngOnInit() { this.loadPapers(); }

  // ── Papers ──────────────────────────────────────────────────────────────────

  loadPapers() {
    this.papersLoading = true;
    this.papersError = '';
    this.paperService.getPapers({ page: this.currentPage, size: 10 }).subscribe({
      next: (res: PageResponse<Paper>) => {
        this.papers = res.content;
        this.totalPages = res.totalPages;
        this.totalElements = res.totalElements;
        this.papersLoading = false;
      },
      error: () => { this.papersError = 'Failed to load papers'; this.papersLoading = false; }
    });
  }

  deletePaper(id: string) {
    if (!confirm('Permanently delete this paper?')) return;
    this.paperService.deletePaper(id).subscribe({
      next: () => this.loadPapers(),
      error: () => alert('Failed to delete paper')
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

  // ── Users ────────────────────────────────────────────────────────────────────

  loadUsers() {
    this.usersLoading = true;
    this.usersError = '';
    this.http.get<User[]>('http://localhost:8080/api/v1/users').subscribe({
      next: (users) => { this.users = users; this.usersLoading = false; },
      error: () => { this.usersError = 'Failed to load users'; this.usersLoading = false; }
    });
  }

  updateRole(userId: string, role: string) {
    this.http.patch(`http://localhost:8080/api/v1/users/${userId}/role`, { role }).subscribe({
      next: () => this.loadUsers(),
      error: () => alert('Failed to update role')
    });
  }

  // ── Courses ──────────────────────────────────────────────────────────────────

  loadCourses() {
    this.coursesLoading = true;
    this.coursesError = '';
    this.courseService.getAllCourses().subscribe({
      next: (courses) => { this.courses = courses; this.coursesLoading = false; },
      error: () => { this.coursesError = 'Failed to load courses'; this.coursesLoading = false; }
    });
  }

  createCourse() {
    if (this.courseForm.invalid) { this.courseForm.markAllAsTouched(); return; }
    this.creatingCourse = true;
    this.courseService.createCourse(this.courseForm.value).subscribe({
      next: () => {
        this.courseForm.reset({ weeks: 4, lessons: 10, price: 0 });
        this.creatingCourse = false;
        this.loadCourses();
      },
      error: () => { alert('Failed to create course'); this.creatingCourse = false; }
    });
  }

  deleteCourse(id: string) {
    if (!confirm('Delete this course and all its enrollments?')) return;
    this.courseService.deleteCourse(id).subscribe({
      next: () => this.loadCourses(),
      error: () => alert('Failed to delete course')
    });
  }

  // ── Tab switching ─────────────────────────────────────────────────────────────

  switchTab(tab: 'papers' | 'users' | 'courses') {
    this.activeTab = tab;
    if (tab === 'users'   && this.users.length === 0)   this.loadUsers();
    if (tab === 'courses' && this.courses.length === 0) this.loadCourses();
  }
}

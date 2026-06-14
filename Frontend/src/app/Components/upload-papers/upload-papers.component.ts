import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PaperDashboardService } from '../../Services/paper_dashboard/paper-dashboard.service';
import { ToastService } from '../../Services/toast/toast.service';

@Component({
  selector: 'app-upload-papers',
  templateUrl: './upload-papers.component.html',
  styleUrls: ['./upload-papers.component.css'],
})
export class UploadPapersComponent {
  form: FormGroup;
  selectedFile: File | null = null;
  uploading = false;

  branches  = ['ECE', 'CS', 'IT', 'ME', 'CE'];
  examTypes = [{ value: 'MID_SEM', label: 'Mid Semester' }, { value: 'END_SEM', label: 'End Semester' }];

  constructor(
    private fb: FormBuilder,
    private paperService: PaperDashboardService,
    private toast: ToastService
  ) {
    this.form = this.fb.group({
      title:    ['', Validators.required],
      subject:  ['', Validators.required],
      branch:   ['', Validators.required],
      year:     ['', [Validators.required, Validators.min(2000), Validators.max(2099)]],
      examType: ['', Validators.required]
    });
  }

  onFileChange(event: any) {
    const file: File = event.target.files[0];
    if (file && file.type !== 'application/pdf') {
      this.toast.show('Only PDF files are allowed', 'error');
      return;
    }
    this.selectedFile = file || null;
  }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    if (!this.selectedFile) { this.toast.show('Please select a PDF file', 'error'); return; }

    const fd = new FormData();
    fd.append('file',     this.selectedFile);
    fd.append('title',    this.form.value.title);
    fd.append('subject',  this.form.value.subject);
    fd.append('branch',   this.form.value.branch);
    fd.append('year',     this.form.value.year.toString());
    fd.append('examType', this.form.value.examType);

    this.uploading = true;
    this.paperService.uploadPaper(fd).subscribe({
      next: () => {
        this.toast.show('Paper uploaded successfully!', 'success');
        this.form.reset();
        this.selectedFile = null;
        this.uploading = false;
      },
      error: (err) => {
        const msg = err?.error?.message || 'Upload failed';
        this.toast.show(msg, 'error');
        this.uploading = false;
      }
    });
  }
}

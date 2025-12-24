import { HttpClient } from '@angular/common/http';
import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastService } from '../../Services/toast/toast.service'; // <-- NEW

@Component({
  selector: 'app-upload-papers',
  templateUrl: './upload-papers.component.html',
  styleUrls: ['./upload-papers.component.css'],
  encapsulation: ViewEncapsulation.ShadowDom
})
export class UploadPapersComponent {

  uploadForm: FormGroup;
  selectedFile: File | null = null;
  // uploading flag removed entirely

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private toast: ToastService // <-- NEW
  ) {
    this.uploadForm = this.fb.group({
      semester: [''],
      year: [''],
      courseName: ['']
    });
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  onSubmit() {
    if (!this.selectedFile) {
      this.toast.show('Please select a file!', 'error');
      return;
    }

    // show toast instantly – no spinner needed
    this.toast.show('Paper uploaded – pending admin approval.', 'success');

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('semester', this.uploadForm.get('semester')?.value);
    formData.append('year', this.uploadForm.get('year')?.value);
    formData.append('courseName', this.uploadForm.get('courseName')?.value);

    // fire-and-forget: reset form immediately
    this.uploadForm.reset();
    this.selectedFile = null;

    // silent background upload (logs only)
    this.http.post('http://localhost:8080/api/files/upload', formData)
      .subscribe({
        next: (res: any) => {
          console.log('[UPLOAD] Server response:', res);
        },
        error: (err) => {
          console.error('[UPLOAD] Server error:', err);
          console.error('[UPLOAD] Error body:', err.error);
        }
      });
  }
}

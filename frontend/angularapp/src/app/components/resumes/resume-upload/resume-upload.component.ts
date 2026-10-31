import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-resume-upload',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './resume-upload.component.html',
  styleUrl: './resume-upload.component.scss'
})
export class ResumeUploadComponent {

  selectedFile!: File;

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadResume(): void {

    if (!this.selectedFile) {
      alert('Please select a file');
      return;
    }

    console.log('Uploading', this.selectedFile.name);

    // API integration later
  }
}
import { Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../api.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { NotificationService } from '../notification.service';
import { MatOption } from '@angular/material/core';

@Component({
  selector: 'app-content-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatOption],
  templateUrl: './content-dialog.component.html',
  styleUrls: ['./content-dialog.component.scss']
})
export class ContentDialogComponent {

  contentForm: FormGroup;
  selectedFile: File | null = null;
  srcResult: any;
  isImage: boolean = false;

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  constructor(
    private dialogRef: MatDialogRef<ContentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { moduleId: number },
    private fb: FormBuilder,
    private apiService: ApiService,
    private notification: NotificationService
  ) {
    this.contentForm = this.fb.group({
      file: [null, Validators.required]
    });
  }

  onFileSelected(): void {
    const inputNode: any = this.fileInput.nativeElement;

    if (inputNode?.files?.length > 0) {
      this.selectedFile = inputNode.files[0];

      // Check file type and set the preview
      if (this.selectedFile?.type.startsWith('image')) {
        this.isImage = true;
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.srcResult = e.target.result;
        };
        reader.readAsDataURL(this.selectedFile); // Read as data URL to preview image
      } else {
        this.isImage = false;
        this.srcResult = null;
      }
    }
  }

  addContent(): void {
    if (!this.selectedFile) {
      return; // Early return if form is invalid or no file selected
    }

    const formData = new FormData();
    formData.append('moduleId', this.data.moduleId.toString());
    formData.append('file', this.selectedFile);
    formData.append('type', this.selectedFile.type);


    this.apiService.addContent(formData).subscribe({
      next: (response) => {
        this.notification.show('Content added successfully');
        this.dialogRef.close(true);
      },
      error: (error) => {
        this.notification.show('Error adding content', true);
      }
    });
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
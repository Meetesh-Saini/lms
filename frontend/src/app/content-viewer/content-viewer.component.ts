import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../api.service';
import { NgIf } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import { MatButton } from '@angular/material/button';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-content-viewer',
  standalone: true,
  imports: [NgIf, NavbarComponent, MatCard, MatCardHeader, MatCardTitle, MatCardActions, MatCardContent, MatButton],
  templateUrl: './content-viewer.component.html',
  styleUrls: ['./content-viewer.component.scss']
})
export class ContentViewerComponent implements OnInit {
  content: any;
  courseId!: number;
  moduleId!: number;
  contentId!: number;
  isFileValid: boolean = true;
  fileUrl: string = '';
  mimeType: string = '';
  isImage: boolean = false;
  isAudio: boolean = false;
  isVideo: boolean = false;
  fullUrl: string = '';

  constructor(
    private apiService: ApiService,
    private route: ActivatedRoute,
    private router: Router,
    private notification: NotificationService,
  ) { }

  ngOnInit(): void {
    // Get route parameters
    this.courseId = +this.route.snapshot.paramMap.get('courseId')!;
    this.moduleId = +this.route.snapshot.paramMap.get('moduleId')!;
    this.contentId = +this.route.snapshot.paramMap.get('contentId')!;
    this.fetchContent();
  }

  fetchContent(): void {
    this.apiService.get(`/contents/${this.contentId}`).subscribe({
      next: (response: any) => {
        this.content = response;
        this.mimeType = this.content.type;
        this.fileUrl = this.content.filePath;
        this.fullUrl = this.apiService.api(true) + this.fileUrl;

        // Determine the type of content (image, video, or audio)
        this.isImage = this.mimeType.startsWith('image/');
        this.isVideo = this.mimeType.startsWith('video/');
        this.isAudio = this.mimeType.startsWith('audio/');

        // If the file is an image, video, or audio, handle rendering
        if (this.isImage || this.isVideo || this.isAudio) {
          this.apiService.getWithOptions(this.fileUrl, { responseType: 'blob' }, true).subscribe(
            {
              next: (response: Blob) => {
                this.fullUrl = URL.createObjectURL(response);
              },
              error: (error) => {
                this.notification.show('Error showing file', true);
              }
            });
          this.isFileValid = true;
        } else {
          // For non-renderable file types, show download option
          this.isFileValid = false;
        }
      },
      error: (error) => {
        console.error('Error fetching content:', error);
      }
    }
    );
  }

  downloadFile(): void {
    // Send HTTP GET request to fetch the file (with headers)
    this.apiService.getWithOptions(this.fileUrl, { responseType: 'blob' }, true).subscribe(
      {
        next: (response: Blob) => {
          // Create a Blob from the response and trigger the download
          const blob = new Blob([response], { type: response.type });
          const link = document.createElement('a');
          link.href = URL.createObjectURL(blob);
          link.download = this.fileUrl.split('/').pop() || 'file';
          link.click();
        },
        error: (error) => {
          this.notification.show('Error downloading file', true);
        }
      });
  }

  goBack(): void {
    this.router.navigate([`/courses/${this.courseId}/modules/${this.moduleId}/content`]);
  }
}

import { Component, InjectionToken } from '@angular/core';

export const SERVICE_URL = new InjectionToken<string>('serviceUrl');

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
}

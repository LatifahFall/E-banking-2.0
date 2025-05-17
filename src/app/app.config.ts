import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { HttpClientModule } from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';

export const appConfig = {
  providers: [
    provideRouter(routes),
    HttpClientModule,        // Add HttpClientModule here
    provideHttpClient()      // Add provideHttpClient() for HttpClient
  ],
};

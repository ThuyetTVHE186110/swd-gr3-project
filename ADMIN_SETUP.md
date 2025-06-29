# PhoneHub Admin - Complete Setup Guide

## Overview

This project is a comprehensive phone shop management system with a modern admin interface featuring:

-   Product management with Cloudinary image upload
-   User management with profile image upload
-   Modern responsive design
-   Real-time search and filtering
-   CRUD operations for products and users

## Cloudinary Setup

### 1. Create Cloudinary Account

1. Go to [cloudinary.com](https://cloudinary.com) and create a free account
2. Note your Cloud Name, API Key, and API Secret from the Dashboard

### 2. Configure Upload Presets

#### Product Images Preset:

1. Go to Settings → Upload → Add upload preset
2. Preset name: `phone-products`
3. Mode: Unsigned
4. Folder: `phonehub/products`
5. Format: Auto
6. Quality: Auto
7. Transformation:
    - Width: 800
    - Height: 800
    - Crop: Fit
8. Save preset

#### User Profile Images Preset:

1. Go to Settings → Upload → Add upload preset
2. Preset name: `user-profiles`
3. Mode: Unsigned
4. Folder: `phonehub/user-profiles`
5. Format: Auto
6. Quality: Auto
7. Transformation:
    - Width: 200
    - Height: 200
    - Crop: Fill
    - Gravity: Face
8. Save preset

### 3. Update Configuration

Edit `src/main/webapp/includes/cloudinary-config.jsp`:

```javascript
window.CLOUDINARY_CONFIG = {
    cloudName: "your-actual-cloud-name", // Replace with your Cloud Name
    uploadPresets: {
        products: "phone-products",
        users: "user-profiles",
    },
    // ... rest of configuration
}
```

## File Structure

```
src/
├── main/
│   ├── java/
│   │   └── swd/project/swdgr3project/
│   │       ├── controller/
│   │       │   ├── AdminServlet.java (Complete admin functionality)
│   │       │   ├── ProductServlet.java
│   │       │   └── ImageUploadServlet.java
│   │       ├── model/
│   │       │   ├── dto/
│   │       │   │   ├── ProductDTO.java
│   │       │   │   └── UserDTO.java (Enhanced with image support)
│   │       │   └── entity/
│   │       │       └── User.java
│   │       ├── service/
│   │       │   ├── ProductService.java
│   │       │   └── UserService.java
│   │       └── utils/
│   │           └── CloudinaryUtils.java
│   └── webapp/
│       ├── adminpage/
│       │   ├── products.jsp (Product management interface)
│       │   ├── product-form.jsp (Add/Edit products with Cloudinary)
│       │   └── users.jsp (User management interface)
│       ├── includes/
│       │   └── cloudinary-config.jsp (Centralized Cloudinary config)
│       ├── css/
│       │   ├── style.css
│       │   └── admin.css (Admin-specific styles)
│       └── js/
│           └── app.js
```

## Features Implemented

### Product Management

-   ✅ Modern responsive table layout
-   ✅ Search and filter functionality
-   ✅ Add/Edit/Delete operations
-   ✅ Cloudinary image upload integration
-   ✅ Image preview and management
-   ✅ Stock level indicators
-   ✅ Category management
-   ✅ Form validation
-   ✅ Success/Error notifications

### User Management

-   ✅ User listing with role badges
-   ✅ Add/Edit/Delete users
-   ✅ Profile image upload via Cloudinary
-   ✅ User status management (Active/Inactive)
-   ✅ Role-based access display
-   ✅ Authentication provider indicators
-   ✅ Search and filter functionality
-   ✅ Modal-based forms

### Cloudinary Integration

-   ✅ Centralized configuration
-   ✅ Product image upload with cropping
-   ✅ User profile image upload
-   ✅ Optimized image transformations
-   ✅ Multiple upload presets
-   ✅ Error handling
-   ✅ Progress indicators

## Admin Interface Features

### Layout

-   Fixed sidebar navigation
-   Responsive design
-   Modern card-based layout
-   Consistent spacing and typography

### Product Management (`/admin/products`)

-   Product listing table with images
-   Search by name, description
-   Filter by category and stock level
-   Add new product button
-   Edit/Delete actions
-   Stock level indicators
-   Image preview

### User Management (`/admin/users`)

-   User listing with profile images
-   Role-based color coding
-   Authentication provider badges
-   Status indicators (Active/Inactive)
-   Search by name, email, username
-   Filter by role and status
-   Modal-based user forms

### Forms

-   Cloudinary upload widgets
-   Real-time image preview
-   Form validation
-   Success/Error feedback
-   Auto-save draft functionality (products)

## API Endpoints

### Product Management

-   `GET /admin/products` - Product management page
-   `GET /admin/product/new` - Add product form
-   `GET /admin/product/{id}` - Edit product form
-   `POST /admin/product/save` - Save product
-   `POST /admin/product/delete` - Delete product

### User Management

-   `GET /admin/users` - User management page
-   `GET /admin/users/api` - Get users JSON
-   `GET /admin/users/api/{id}` - Get user by ID JSON
-   `POST /admin/user/save` - Create user
-   `POST /admin/user/update` - Update user
-   `POST /admin/user/delete` - Delete user
-   `POST /admin/user/toggle-status` - Toggle user status

## Usage Instructions

### Adding a Product

1. Navigate to `/admin/products`
2. Click "Add New Product"
3. Fill in product details
4. Click "Upload" to add images via Cloudinary
5. Preview images before saving
6. Click "Save Product"

### Managing Users

1. Navigate to `/admin/users`
2. Click "Add New User" for new users
3. Click edit icon to modify existing users
4. Upload profile images via Cloudinary
5. Toggle user status as needed
6. Delete users (except admins)

### Image Management

-   All images are automatically optimized
-   Products: 800x800px, auto format/quality
-   User profiles: 200x200px, face-centered crop
-   Cropping interface available during upload
-   Supports JPG, PNG, WebP, GIF formats

## Security Features

-   Form validation on both client and server side
-   CSRF protection (implement as needed)
-   Role-based access control
-   File type validation for uploads
-   Size limits on uploads

## Responsive Design

-   Mobile-friendly interface
-   Collapsible sidebar on small screens
-   Touch-friendly buttons and controls
-   Optimized table layouts
-   Modal dialogs adapt to screen size

## Browser Support

-   Chrome (recommended)
-   Firefox
-   Safari
-   Edge
-   Mobile browsers

## Next Steps

1. Implement real UserService integration
2. Add order management functionality
3. Implement authentication and authorization
4. Add data export features
5. Implement real-time notifications
6. Add bulk operations
7. Implement product categories management
8. Add inventory tracking
9. Implement sales analytics
10. Add backup and restore features

## Troubleshooting

### Cloudinary Issues

-   Verify cloud name and upload presets
-   Check browser console for errors
-   Ensure upload presets are set to "unsigned"
-   Verify network connectivity

### Image Upload Problems

-   Check file size limits (5MB max)
-   Verify supported formats
-   Check browser permissions for camera/file access
-   Clear browser cache if configuration changes

### UI Issues

-   Verify CSS files are loading
-   Check for JavaScript errors in console
-   Ensure all required dependencies are included
-   Test in different browsers

## Development Notes

-   Mock data is currently used for user management
-   Replace mock implementations with real service calls
-   Add proper error handling and logging
-   Implement database transactions
-   Add unit and integration tests
-   Consider implementing caching for better performance

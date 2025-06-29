<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Cloudinary Configuration</title>
        </head>

        <body>
            <script>
                // Cloudinary Configuration
                window.CLOUDINARY_CONFIG = {
                    cloudName: 'phonehub', // Replace with your Cloudinary cloud name
                    uploadPresets: {
                        products: 'phone-products', // Replace with your product upload preset
                        users: 'user-profiles' // Replace with your user upload preset
                    },
                    folders: {
                        products: 'phonehub/products',
                        users: 'phonehub/user-profiles'
                    },
                    maxFileSize: 5000000, // 5MB
                    allowedFormats: ['jpg', 'jpeg', 'png', 'webp', 'gif'],
                    transformations: {
                        product: {
                            width: 800,
                            height: 800,
                            crop: 'fit',
                            quality: 'auto',
                            format: 'auto'
                        },
                        productThumbnail: {
                            width: 200,
                            height: 200,
                            crop: 'fill',
                            quality: 'auto',
                            format: 'auto'
                        },
                        userProfile: {
                            width: 200,
                            height: 200,
                            crop: 'fill',
                            gravity: 'face',
                            quality: 'auto',
                            format: 'auto'
                        }
                    }
                };

                // Cloudinary Upload Widget Helper Functions
                window.CloudinaryUploader = {
                    createProductUploader: function (onSuccess, onError) {
                        return cloudinary.createUploadWidget({
                            cloudName: window.CLOUDINARY_CONFIG.cloudName,
                            uploadPreset: window.CLOUDINARY_CONFIG.uploadPresets.products,
                            folder: window.CLOUDINARY_CONFIG.folders.products,
                            maxFileSize: window.CLOUDINARY_CONFIG.maxFileSize,
                            clientAllowedFormats: window.CLOUDINARY_CONFIG.allowedFormats,
                            cropping: true,
                            croppingAspectRatio: 1,
                            croppingValidateDimensions: true,
                            sources: ['local', 'url', 'camera'],
                            multiple: false,
                            theme: 'minimal',
                            styles: {
                                palette: {
                                    window: '#FFFFFF',
                                    sourceBg: '#F4F4F5',
                                    windowBorder: '#90A0B3',
                                    tabIcon: '#0E2F5A',
                                    inactiveTabIcon: '#69778A',
                                    menuIcons: '#5A616A',
                                    link: '#0E2F5A',
                                    action: '#0E2F5A',
                                    inProgress: '#0E2F5A',
                                    complete: '#0E2F5A',
                                    error: '#EA5F5F',
                                    textDark: '#000000',
                                    textLight: '#FFFFFF'
                                }
                            }
                        }, function (error, result) {
                            if (!error && result && result.event === 'success') {
                                if (onSuccess) onSuccess(result.info);
                            } else if (error && onError) {
                                onError(error);
                            }
                        });
                    },

                    createUserUploader: function (onSuccess, onError) {
                        return cloudinary.createUploadWidget({
                            cloudName: window.CLOUDINARY_CONFIG.cloudName,
                            uploadPreset: window.CLOUDINARY_CONFIG.uploadPresets.users,
                            folder: window.CLOUDINARY_CONFIG.folders.users,
                            maxFileSize: window.CLOUDINARY_CONFIG.maxFileSize,
                            clientAllowedFormats: window.CLOUDINARY_CONFIG.allowedFormats,
                            cropping: true,
                            croppingAspectRatio: 1,
                            croppingValidateDimensions: true,
                            sources: ['local', 'url', 'camera'],
                            multiple: false,
                            theme: 'minimal',
                            styles: {
                                palette: {
                                    window: '#FFFFFF',
                                    sourceBg: '#F4F4F5',
                                    windowBorder: '#90A0B3',
                                    tabIcon: '#0E2F5A',
                                    inactiveTabIcon: '#69778A',
                                    menuIcons: '#5A616A',
                                    link: '#0E2F5A',
                                    action: '#0E2F5A',
                                    inProgress: '#0E2F5A',
                                    complete: '#0E2F5A',
                                    error: '#EA5F5F',
                                    textDark: '#000000',
                                    textLight: '#FFFFFF'
                                }
                            }
                        }, function (error, result) {
                            if (!error && result && result.event === 'success') {
                                if (onSuccess) onSuccess(result.info);
                            } else if (error && onError) {
                                onError(error);
                            }
                        });
                    },

                    // Generate optimized URLs
                    generateUrl: function (publicId, transformation) {
                        const config = window.CLOUDINARY_CONFIG;
                        let url = `https://res.cloudinary.com/${config.cloudName}/image/upload/`;

                        if (transformation && config.transformations[transformation]) {
                            const transform = config.transformations[transformation];
                            const transformParams = Object.keys(transform)
                                .map(key => `${key}_${transform[key]}`)
                                .join(',');
                            url += `${transformParams}/`;
                        }

                        return url + publicId;
                    },

                    // Get optimized product image URL
                    getProductImageUrl: function (publicId, size = 'product') {
                        return this.generateUrl(publicId, size === 'thumbnail' ? 'productThumbnail' : 'product');
                    },

                    // Get optimized user profile image URL
                    getUserProfileImageUrl: function (publicId) {
                        return this.generateUrl(publicId, 'userProfile');
                    }
                };
            </script>
        </body>

        </html>
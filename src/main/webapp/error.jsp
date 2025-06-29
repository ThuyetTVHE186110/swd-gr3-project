<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Error - PhoneHub</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        </head>

        <body>
            <!-- Header -->
            <header class="header">
                <nav class="navbar">
                    <a href="${pageContext.request.contextPath}/" class="logo">
                        <i class="fas fa-mobile-alt"></i> PhoneHub
                    </a>
                </nav>
            </header>

            <!-- Error Content -->
            <main class="container">
                <div class="error-page">
                    <div class="error-content">
                        <i class="fas fa-exclamation-triangle error-icon"></i>
                        <h1>Oops! Something went wrong</h1>
                        <p class="error-message">
                            <c:choose>
                                <c:when test="${not empty error}">
                                    ${error}
                                </c:when>
                                <c:otherwise>
                                    An unexpected error occurred. Please try again later.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="error-actions">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                                <i class="fas fa-home"></i> Go Home
                            </a>
                            <button onclick="history.back()" class="btn btn-secondary">
                                <i class="fas fa-arrow-left"></i> Go Back
                            </button>
                        </div>
                    </div>
                </div>
            </main>

            <style>
                .error-page {
                    min-height: 60vh;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    text-align: center;
                    padding: 2rem;
                }

                .error-content {
                    max-width: 500px;
                }

                .error-icon {
                    font-size: 4rem;
                    color: #f59e0b;
                    margin-bottom: 1rem;
                }

                .error-page h1 {
                    font-size: 2rem;
                    margin-bottom: 1rem;
                    color: #1f2937;
                }

                .error-message {
                    font-size: 1.1rem;
                    color: #6b7280;
                    margin-bottom: 2rem;
                    line-height: 1.6;
                }

                .error-actions {
                    display: flex;
                    gap: 1rem;
                    justify-content: center;
                    flex-wrap: wrap;
                }

                .error-actions .btn {
                    display: inline-flex;
                    align-items: center;
                    gap: 0.5rem;
                    padding: 0.75rem 1.5rem;
                    border-radius: 0.5rem;
                    text-decoration: none;
                    font-weight: 500;
                    transition: all 0.2s ease;
                    border: none;
                    cursor: pointer;
                }

                .btn-primary {
                    background-color: #3b82f6;
                    color: white;
                }

                .btn-primary:hover {
                    background-color: #2563eb;
                }

                .btn-secondary {
                    background-color: #6b7280;
                    color: white;
                }

                .btn-secondary:hover {
                    background-color: #4b5563;
                }
            </style>
        </body>

        </html>
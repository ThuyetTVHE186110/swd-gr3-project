# 📸 Cloudinary Setup Guide for PhoneHub Admin

This guide will walk you through setting up Cloudinary for image management in your PhoneHub application.

## 🚀 Step 1: Create a Cloudinary Account

1. **Visit Cloudinary Website**

    - Go to [https://cloudinary.com](https://cloudinary.com)
    - Click on "Sign Up for Free"

2. **Sign Up Options**

    - **Option A: Email Sign-up**

        - Enter your email address
        - Create a strong password
        - Enter your first and last name
        - Choose "Developer" as your role
        - Click "Create Account"

    - **Option B: GitHub Sign-up**

        - Click "Sign up with GitHub"
        - Authorize Cloudinary to access your GitHub account

    - **Option C: Google Sign-up**
        - Click "Sign up with Google"
        - Select your Google account

3. **Email Verification**
    - Check your email for a verification link
    - Click the verification link to activate your account

## 🔧 Step 2: Get Your API Credentials

1. **Access Dashboard**

    - After signing up, you'll be redirected to your dashboard
    - If not, go to [https://cloudinary.com/console](https://cloudinary.com/console)

2. **Find Your Credentials**

    - On the dashboard, you'll see a "Product Environment Credentials" section
    - **Important:** You'll see three key pieces of information:
        ```
        Cloud name: your-cloud-name
        API Key: 123456789012345
        API Secret: AbCdEfGhIjKlMnOpQrStUvWxYz
        ```

3. **Copy Your Credentials**
    - **Cloud Name**: This is your unique identifier (e.g., `dh8example`)
    - **API Key**: A public identifier (e.g., `123456789012345`)
    - **API Secret**: A private key (keep this secret!)

## 🔐 Step 3: Set Up Upload Presets

Upload presets define how your images will be processed when uploaded.

1. **Navigate to Upload Presets**

    - In your Cloudinary dashboard, go to **Settings** → **Upload**
    - Click on **Upload presets**

2. **Create Product Images Preset**

    - Click **Add upload preset**
    - **Preset name**: `product-images`
    - **Signing mode**: Select **Unsigned** (for frontend uploads)
    - **Folder**: `phonehub/products`
    - **Format**: `Auto`
    - **Quality**: `Auto:Good`
    - **Max file size**: `5 MB`
    - **Allowed formats**: `jpg, jpeg, png, webp`
    - **Transformation**: Add these transformations:
        - Width: 800, Height: 800, Crop: Fill
        - Quality: Auto:Good
        - Format: Auto
    - Click **Save**

3. **Create User Profiles Preset**
    - Click **Add upload preset**
    - **Preset name**: `user-profiles`
    - **Signing mode**: Select **Unsigned**
    - **Folder**: `phonehub/user-profiles`
    - **Format**: `Auto`
    - **Quality**: `Auto:Good`
    - **Max file size**: `3 MB`
    - **Allowed formats**: `jpg, jpeg, png`
    - **Transformation**: Add these transformations:
        - Width: 400, Height: 400, Crop: Fill
        - Quality: Auto:Good
        - Format: Auto
    - Click **Save**

## ⚙️ Step 4: Configure Your Application

### Option A: Environment Variables (Recommended)

1. **Set Environment Variables**

    **Windows (Command Prompt):**

    ```cmd
    setx CLOUDINARY_CLOUD_NAME "your-actual-cloud-name"
    setx CLOUDINARY_API_KEY "your-actual-api-key"
    setx CLOUDINARY_API_SECRET "your-actual-api-secret"
    ```

    **Windows (PowerShell):**

    ```powershell
    [Environment]::SetEnvironmentVariable("CLOUDINARY_CLOUD_NAME", "your-actual-cloud-name", "User")
    [Environment]::SetEnvironmentVariable("CLOUDINARY_API_KEY", "your-actual-api-key", "User")
    [Environment]::SetEnvironmentVariable("CLOUDINARY_API_SECRET", "your-actual-api-secret", "User")
    ```

    **Linux/Mac:**

    ```bash
    export CLOUDINARY_CLOUD_NAME="your-actual-cloud-name"
    export CLOUDINARY_API_KEY="your-actual-api-key"
    export CLOUDINARY_API_SECRET="your-actual-api-secret"
    ```

    **Add to your ~/.bashrc or ~/.profile:**

    ```bash
    echo 'export CLOUDINARY_CLOUD_NAME="your-actual-cloud-name"' >> ~/.bashrc
    echo 'export CLOUDINARY_API_KEY="your-actual-api-key"' >> ~/.bashrc
    echo 'export CLOUDINARY_API_SECRET="your-actual-api-secret"' >> ~/.bashrc
    source ~/.bashrc
    ```

2. **Restart your IDE/Terminal**
    - Close and reopen your IDE (VS Code, IntelliJ, etc.)
    - Restart your terminal/command prompt

### Option B: Configuration File

1. **Update cloudinary.properties**

    - Edit `src/main/resources/cloudinary.properties`

    ```properties
    cloudinary.cloud_name=your-actual-cloud-name
    cloudinary.api_key=your-actual-api-key
    cloudinary.api_secret=your-actual-api-secret
    ```

2. **Update CloudinaryUtils.java** (if not using environment variables)
    ```java
    // Replace the static block in CloudinaryUtils.java
    static {
        Properties props = new Properties();
        try (InputStream input = CloudinaryUtils.class.getClassLoader()
                 .getResourceAsStream("cloudinary.properties")) {
            props.load(input);
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", props.getProperty("cloudinary.cloud_name"),
                "api_key", props.getProperty("cloudinary.api_key"),
                "api_secret", props.getProperty("cloudinary.api_secret")
            ));
        } catch (IOException e) {
            log.error("Failed to load Cloudinary configuration", e);
        }
    }
    ```

## 🌐 Step 5: Update Frontend Configuration

1. **Update Cloudinary Config in JSP**
    - Edit `src/main/webapp/includes/cloudinary-config.jsp`
    ```jsp
    <%
    // Replace with your actual cloud name
    String cloudName = "your-actual-cloud-name";
    %>
    <script>
    window.cloudinaryConfig = {
        cloudName: '<%= cloudName %>',
        uploadPresets: {
            products: 'product-images',
            userProfiles: 'user-profiles'
        }
    };
    </script>
    ```

## 🧪 Step 6: Test Your Configuration

1. **Test Backend Integration**

    ```java
    // Add this test method to verify configuration
    public static void testConfiguration() {
        if (isConfigured()) {
            System.out.println("✅ Cloudinary is properly configured!");
            System.out.println("Cloud Name: " + cloudinary.config.cloudName);
        } else {
            System.out.println("❌ Cloudinary configuration missing!");
        }
    }
    ```

2. **Test Frontend Upload**
    - Start your application
    - Go to `/admin/products`
    - Click "Add New Product"
    - Try uploading an image using the upload button
    - Check if the image URL is populated

## 🔍 Step 7: Troubleshooting

### Common Issues:

1. **"Invalid API Key" Error**

    - Double-check your API key and secret
    - Ensure no extra spaces or characters
    - Verify environment variables are set correctly

2. **"Upload preset not found" Error**

    - Verify upload preset names match exactly
    - Ensure upload presets are set to "Unsigned"
    - Check spelling in your JSP files

3. **"Invalid cloud name" Error**

    - Verify your cloud name is correct
    - Cloud names are case-sensitive
    - Don't include spaces or special characters

4. **Images not displaying**
    - Check browser console for errors
    - Verify the image URLs are valid
    - Ensure CORS is properly configured

### Debug Steps:

1. **Check Environment Variables**

    ```cmd
    echo %CLOUDINARY_CLOUD_NAME%
    echo %CLOUDINARY_API_KEY%
    ```

2. **Verify Upload Presets**

    - Go to Cloudinary dashboard → Settings → Upload → Upload presets
    - Ensure your presets exist and are unsigned

3. **Test API Connection**
    ```java
    // Add to a test method
    try {
        Map result = cloudinary.api().ping();
        System.out.println("Cloudinary API connection successful: " + result);
    } catch (Exception e) {
        System.out.println("Cloudinary API connection failed: " + e.getMessage());
    }
    ```

## 🚀 Step 8: Go Live!

Once everything is working:

1. **Restart your application**
2. **Test all upload functionality**:
    - Product image uploads
    - User profile image uploads
    - Image transformations
3. **Monitor your Cloudinary dashboard** for uploaded images
4. **Check usage limits** on your free plan

## 📊 Free Plan Limits

Cloudinary free plan includes:

-   **25 GB** storage
-   **25 GB** monthly bandwidth
-   **25,000** transformations per month
-   **1,000** uploads per month

Perfect for development and small applications!

## 🎉 You're All Set!

Your PhoneHub application now has professional image management capabilities with:

-   ✅ Automatic image optimization
-   ✅ On-the-fly transformations
-   ✅ CDN delivery
-   ✅ Easy upload interface
-   ✅ Responsive images

Happy coding! 🚀📱

package designprinciples.codequality;


/*
===============================================================================
                        CODE QUALITY PRINCIPLES
===============================================================================
These principles help write code that is:
- Easy to read
- Easy to maintain
- Easy to extend
- Less error-prone
*/


/*
===============================================================================
1. DRY (Don't Repeat Yourself)
===============================================================================
- Every piece of knowledge or business logic should exist in only one place.
- Avoid duplicating the same code or logic.
- If the logic changes, update it in one location instead of many.
- DRY reduces bugs and makes maintenance easier.
*/
class DiscountService {

    private double applyDiscount(double price, double discountPercentage) {
        return price * (1 - discountPercentage);
    }

    //applyDiscount method used at two places instead of writing same logic or code there
    public double studentDiscount(double price) {
        return applyDiscount(price, 0.20);
    }

    public double employeeDiscount(double price) {
        return applyDiscount(price, 0.20);
    }
}


/*
===============================================================================
2. KISS (Keep It Simple, Stupid)
===============================================================================
- Prefer the simplest solution that correctly solves the problem.
- Avoid unnecessary classes, patterns, or complex logic.
- Simple code is easier to understand, debug, and maintain.
- Simplicity does NOT mean sacrificing readability or functionality.
*/
class LoginValidator {

    public boolean isValid(String username) {

        //step-by-step easy-to-read validations 
        //instead of complex conditon like (username != null && username.length() >= 5 && username.matches("[A-Za-z0-9]+")
        if (username == null)
            return false;

        if (username.length() < 5)
            return false;

        return username.matches("[A-Za-z0-9]+");
    }
}


/*
===============================================================================
3. YAGNI (You Aren't Gonna Need It)
===============================================================================
- Don't implement features until they are actually required.
- Avoid writing code based on future assumptions.
- Build only what is needed today.
- Future requirements may change, making unused code unnecessary.
*/
class UserService {

    //Only registerUser() is currently required
    public void registerUser() {
        System.out.println("User Registered");
    }

    //Add deleteUser(), exportToPDF(), AIRecommendation(), etc.
    //only when they are actually required.
}


/*
===============================================================================
4. POLA (Principle Of Least Astonishment)
===============================================================================
- Code should behave exactly as another developer expects.
- Avoid surprising or misleading behavior.
- Method names, return values, and side effects should be predictable.
- This improves readability and reduces bugs.
*/
class Calculator {

    public int add(int a, int b) {
        return a + b;     // Expected behavior
    }
}


class CodeQualityMain {

    public static void main(String[] args) {

        // DRY
        DiscountService discountService = new DiscountService();
        System.out.println(discountService.studentDiscount(100));

        // KISS
        LoginValidator validator = new LoginValidator();
        System.out.println(validator.isValid("Dhruv123"));

        // YAGNI
        UserService userService = new UserService();
        userService.registerUser();

        // POLA
        Calculator calculator = new Calculator();
        System.out.println(calculator.add(10, 20));
    }
}

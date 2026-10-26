# 🔄 Unit Converter Studio

A sleek, modern, and robust desktop Unit Converter application built **100% with pure Java Swing** and **Java 2D vector graphics**.

![Java](https://img.shields.io/badge/Java-21%2B%20%2F%20Swing-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)

---

## ✨ Features

- 🔐 **Modern Authentication / Sign-In Card**:
  - Elevated shadow card container with smooth rounded corners.
  - Live Gmail validation (`user@gmail.com`) with instant feedback.
  - Interactive password field with show/hide toggle.
- 📐 **10 Comprehensive Conversion Categories**:
  - **Length**: Meter, Centimeter, Millimeter, Kilometer, Inch, Foot, Yard, Mile, Nautical Mile
  - **Mass / Weight**: Kilogram, Gram, Milligram, Metric Ton, Pound, Ounce, Stone
  - **Temperature**: Celsius (°C), Fahrenheit (°F), Kelvin (K)
  - **Volume**: Liter, Milliliter, Cubic meter, US Gallon, US Quart, US Pint, Fluid Ounce
  - **Area**: Square meter, Square kilometer, Square foot, Square yard, Acre, Hectare
  - **Speed**: Meter per second, Kilometer per hour, Mile per hour, Knot
  - **Time**: Second, Minute, Hour, Day, Week, Year
  - **Digital Storage**: Byte, Kilobyte, Megabyte, Gigabyte, Terabyte, Petabyte
  - **Pressure**: Pascal, Kilopascal, Bar, PSI, Atmosphere
  - **Energy**: Joule, Kilojoule, Calorie, Kilocalorie, Watt-hour, Kilowatt-hour
- 🧮 **Real-Time Live Calculation & Formula Display**:
  - Instant calculation as you type without pressing "Calculate".
  - Shows explicit mathematical formula breakdown and conversion factors.
- 🔁 **Quick Controls**:
  - One-click swap button to reverse From $\leftrightarrow$ To units.
  - One-click copy result to clipboard.
- 📜 **Conversion History Log**:
  - Automatically records conversion history with timestamps and categories during the session.

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK 8, 17, 21, or 26)**
- Any terminal / command prompt or IDE (IntelliJ IDEA, Eclipse, VS Code).

### Compile and Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jitendrarramesh-arch/UnitConverter.git
   cd UnitConverter
   ```

2. **Compile the application:**
   ```bash
   javac -d out/production/Basicconverter src/BasicConverter.java
   ```

3. **Run the application:**
   ```bash
   java -cp out/production/Basicconverter BasicConverter
   ```

---

## 🎨 UI & Design System

- **Clean Typography**: Uses anti-aliased Segoe UI fonts with high-DPI scaling.
- **Color Themes**: Dynamic accent colors tailored for each category (Cyan, Amber, Red, Emerald, Lime, Orange, Violet, Indigo, Pink, Gold).
- **Pure Vector Icons**: Crisp user, mail, lock, swap, eye toggle, and copy icons rendered purely in Java 2D without external image assets.

---

## 📄 License

This project is licensed under the MIT License - feel free to use and modify!

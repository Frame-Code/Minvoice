# 🧾 Proyecto Personal: Gestor de Finanzas y Facturas (JavaFX + Spring)

## 📘 Descripción general
Aplicación **de escritorio** desarrollada en **JavaFX** con **Spring Framework**  
para administrar mis finanzas personales y controlar mis facturas.

El sistema permitirá registrar facturas, subir sus archivos (PDF o imagen),
distinguir entre las **pagadas** y las **pendientes**, y calcular los **totales** automáticamente.

---

## 🎯 Objetivos principales
- Centralizar mis facturas personales en una sola aplicación.
- Identificar rápidamente qué facturas ya fueron **canceladas**.
- Subir archivos asociados (comprobantes o facturas en PDF/imágenes).
- Mostrar totales de manera automática.
- Mantener todos los datos **localmente** (sin conexión externa).

---

## 🧩 Funcionalidades principales

### 1. Gestión de facturas
- Crear una factura con los datos:
  - Descripción
  - Monto
  - Fecha
  - Estado (Pendiente / Pagada)
  - Archivo (PDF o imagen)
- Editar y eliminar facturas existentes.
- Ver lista completa de facturas en una tabla dentro de la UI.
- Filtros:
  - Por estado: `Todas | Pendientes | Pagadas`
  - Por texto: buscar en la descripción o fecha.

### 2. Resumen general
- Mostrar **dashboard superior** con:
  - 💰 Total facturado
  - ✅ Total pagado
  - 🕐 Total pendiente
  - 📦 Cantidad total de facturas
- Totales se recalculan automáticamente cada vez que se agrega o modifica una factura.

### 3. Interfaz de usuario (JavaFX)
- Estilo limpio y moderno (FXML + CSS).
- Layout principal con **BorderPane** o **VBox**:
  - Parte superior → resumen (dashboard).
  - Parte central → tabla de facturas.
  - Parte inferior o lateral → botones de acción.
- Colores visuales:
  - Verde → Factura pagada
  - Amarillo → Factura pendiente
- Iconos simples (usando FontAwesomeFX o MaterialFX).
- Ventana modal o diálogo para subir nuevas facturas.

### 4. Subida de facturas
- Formulario modal (FXML) para ingresar:
  - Descripción (TextField)
  - Monto (TextField numérico)
  - Fecha (DatePicker)
  - Archivo (FileChooser)
  - Estado inicial (ComboBox: Pagada / Pendiente)
- Validaciones:
  - Campos requeridos.
  - Monto debe ser numérico positivo.
  - Fecha no puede estar vacía.
- Posibilidad de arrastrar y soltar archivos (Drag & Drop) sobre el formulario.

### 5. Detalle de factura (opcional)
- Ventana o panel adicional que muestre la información completa.
- Botón para abrir el archivo PDF o imagen en el visor por defecto del sistema.
- Opción para cambiar estado directamente desde el detalle.

### 6. Estadísticas (futuro)
- Mostrar gráficas simples con JavaFX `PieChart` o `BarChart`:
  - Facturas por estado.
  - Total mensual.
  - Porcentaje pagado vs pendiente.

---

## ⚙️ Requisitos técnicos

### 🧠 Arquitectura
- **Capa de presentación:** JavaFX (FXML + Controladores).
- **Capa de servicio:** Spring Framework (`@Service`).
- **Capa de persistencia:** Spring Data JPA + SQLite (o H2 embebida).
- **Capa de entidad:** POJOs con anotaciones JPA.

Estructura sugerida:


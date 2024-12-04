import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class MaquinaExpendedora implements Carrito {

	//Test
	 //Crear un HashSet para los productos que ya se compraron
	 HashSet<String> productosIncluidosVenta = new HashSet<>();
	 ArrayList<Producto> productos = new ArrayList<>();
	    ArrayList<String> ventasDelDia = new ArrayList<>();
	    double total = 0;

	    public MaquinaExpendedora() {
	        
	        productos.add(new Alimento("1", "Gansitos", 10.5, 20));
	        productos.add(new Alimento("2", "Ruffles", 8.75, 20));
	        productos.add(new Bebida("3", "Coca-Cola", 15, 20));
	        productos.add(new Bebida("4", "Pepsi", 14, 20));
	    }

		//Se mejoro la vista de la tabla de productos
	    public void mostrarProductos() {
			System.out.println("Productos disponibles:");
		
			
			System.out.printf("%-10s| %-20s| %-10s| %-10s| %-10s%n", "Código", "Descripción", "Precio", "Tipo", "Inventario");
		
			
			for (Producto p : productos) {
				
				System.out.printf("%-10s| %-20s| %-10.2f| %-10s| %-10d%n", 
					p.codigo, 
					p.descripcion, 
					p.precio, 
					p.tipo(), 
					p.inventario);
			}
		}

	    public void agregarProducto(String codigo, int cantidad) {
	        for (Producto p : productos) {
	            if (p.codigo.equals(codigo)) {
	                if (p instanceof Alimento) {
	                    if (p.inventario + cantidad > 20) {
	                        System.out.println("El contenedor de alimentos está lleno.");
	                        return;
	                    }
	                } else if (p instanceof Bebida) {
	                    if (p.inventario + cantidad > 20) {
	                        System.out.println("El contenedor de bebidas está lleno.");
	                        return;
	                    }
	                }
	                p.inventario += cantidad;
	                System.out.println("Se han agregado " + cantidad + " unidades de " + p.descripcion + " al inventario.");
	                return;
	            }
	        }
	        System.out.println("Producto no encontrado.");
	    }

	    public void agregarProducto(Producto producto) {
	        if (producto.inventario > 0) {
	            total += producto.precio;
	            producto.inventario--;
	            System.out.println("Se ha agregado " + producto.descripcion + " al carrito.");
	        } else {
	            System.out.println("No hay inventario disponible para " + producto.descripcion);
	        }
	    }

	    public void finalizarCompra() {
	        if (total == 0) {
	            System.out.println("No hay productos en el carrito.");
	            return;
	        }

	        System.out.println("\nTotal a pagar: " + total);
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Ingrese la cantidad con la que paga: ");
	        double pago = scanner.nextDouble();
	        if (pago < total) {
	            System.out.println("El pago es insuficiente.");
	        } else {
	            double cambio = pago - total;
	            System.out.println("Cambio a devolver: " + cambio);
	            // Generar comprobante
	            generarComprobante();
	            // Registrar venta del día
	            registrarVenta();
				//Quite donde se reinicia el inventario
	            // Reiniciar carrito y total
	            //productos.forEach(producto -> producto.inventario = 0);
	            total = 0;
	        }
	    }
//cambiado
	    private void generarComprobante() {
			// Obtener la fecha y hora actual
			Date fechaHoraActual = new Date();
			// Formatear la fecha y hora actual
			SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String fechaHora = formatoFechaHora.format(fechaHoraActual);

			try {
				FileWriter writer = new FileWriter("comprobante.txt",true);
				 // Escribir la fecha y hora
				 writer.write("Fecha y hora: " + fechaHora + "\n");
				writer.write("Productos comprados:\n");
				for (Producto p : productos) {
					if (p.inventario < 20 ) {
						writer.write(p.descripcion + "\n");
						
						
					}
				}
				//Se agrega un salto de linea despues del total
				writer.write("Total: " + total+ "\n");
				writer.write("\n");
				writer.close();
				System.out.println("Se ha generado el comprobante.");
			} catch (IOException e) {
				System.out.println("Error al generar el comprobante.");
			}
		}
//cambiado
		private void registrarVenta() {
			// Obtener la fecha y hora actual
			Date fechaHoraActual = new Date();
			// Formatear la fecha y hora actual
			SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String fechaHora = formatoFechaHora.format(fechaHoraActual);
		
			// Crear la cadena de venta del día
			StringBuilder venta = new StringBuilder();
			venta.append("Fecha y hora: ").append(fechaHora).append("\n");
			venta.append("Productos comprados:\n");
			
			for (Producto p : productos) {
				if (p.inventario < 20 && !productosIncluidosVenta.contains(p.descripcion)) {
					venta.append(p.descripcion).append("\n");
					productosIncluidosVenta.add(p.descripcion); // Marca este producto como registrado
				}
			}
		
			venta.append("Total: ").append(total).append("\n");
		
			// Agregar la venta del día a la lista de ventas
			ventasDelDia.add(venta.toString());
		}
	    public void mostrarVentasDelDia() {

	        if (ventasDelDia.isEmpty()) {
	            System.out.println("No hay ventas registradas hoy.");
	            return;
	        }

	        System.out.println("Ventas del día:");
	        for (String venta : ventasDelDia) {
	            System.out.println(venta);
	        }
	    }
	    
	    
}

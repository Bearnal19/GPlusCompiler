/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador;

/**
 *
 * @author Neto
 */
public class EtiqRedir {
    private String ini;
	private String fin;

	public EtiqRedir(String e1, String e2) {
		ini = e1;
		fin = e2;
	}

	public void ini(String e1) {
		ini = e1;
	}

	public void fin(String e2) {
		fin = e2;
	}

	public String ini() {
		return ini;
	}

	public String fin() {
		return fin;
	}
}

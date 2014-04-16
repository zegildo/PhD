package entities;

import java.util.List;
import java.util.Map;

public class EmpresasCotacoes {

	private Empresas empresa;
	private Map<String,List<Cotacoes>> cotacoes;
	
	public EmpresasCotacoes(Empresas empresa, Map<String, List<Cotacoes>> cotacoes) {
		this.empresa = empresa;
		this.cotacoes = cotacoes;
	}

	public Empresas getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresas empresa) {
		this.empresa = empresa;
	}

	public Map<String, List<Cotacoes>> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(Map<String, List<Cotacoes>> cotacoes) {
		this.cotacoes = cotacoes;
	}
	
}

package br.com.richard.model;


public enum FormaPagamento {
	DINHEIRO("Dinheiro"), 
	CARTAO_CREDITO("Cartão de credito"), 
	CARTAO_DEBITO("Cartão de débito"), 
	CHEQUE("Cheque"), 
	BOLETO_BANCARIO("Boleto bancário"), 
	DEPOSITO_BANCARI("Depósito bancário");
	
	private String descricao;

	private FormaPagamento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
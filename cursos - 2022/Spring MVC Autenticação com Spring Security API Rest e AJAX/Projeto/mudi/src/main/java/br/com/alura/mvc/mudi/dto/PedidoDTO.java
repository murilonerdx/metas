package br.com.alura.mvc.mudi.dto;

import br.com.alura.mvc.mudi.entities.Pedido;
import br.com.alura.mvc.mudi.entities.enums.StatusPedido;

import javax.validation.constraints.NotBlank;

public class PedidoDTO {
    private Long id;

    @NotBlank
    private String nomeProduto;

    @NotBlank
    private String urlProduto;

    @NotBlank
    private String urlImagem;

    private String descricao;
    private StatusPedido status;

    public PedidoDTO(Long id, String nomeProduto, String urlProduto, String urlImagem, String descricao, StatusPedido status) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.urlProduto = urlProduto;
        this.urlImagem = urlImagem;
        this.descricao = descricao;
        this.status = status;
    }

    public PedidoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getUrlProduto() {
        return urlProduto;
    }

    public void setUrlProduto(String urlProduto) {
        this.urlProduto = urlProduto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public PedidoDTO toDto(Pedido pedido){
        this.descricao = pedido.getDescricao();
        this.status = pedido.getStatus();
        this.nomeProduto = pedido.getNomeProduto();
        this.urlImagem = pedido.getUrlImagem();
        this.id = pedido.getId();
        return this;
    }
}

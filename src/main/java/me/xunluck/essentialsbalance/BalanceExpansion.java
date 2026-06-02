package me.xunluck.essentialsbalance;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class BalanceExpansion extends PlaceholderExpansion {

    private static final String IDENTIFIER = "essbalance";
    private static final String ZERO = "0";

    private static final double THOUSAND = 1_000.0;
    private static final double MILLION = 1_000_000.0;
    private static final double BILLION = 1_000_000_000.0;
    private static final double TRILLION = 1_000_000_000_000.0;

    private final EssentialsBalancePlaceholder plugin;
    private final Essentials essentials;
    private final DecimalFormat decimalFormat;
    private final DecimalFormat wholeNumberFormat;

    public BalanceExpansion(EssentialsBalancePlaceholder plugin, Essentials essentials) {
        this.plugin = plugin;
        this.essentials = essentials;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        this.decimalFormat = new DecimalFormat("#,##0.00", symbols);
        this.wholeNumberFormat = new DecimalFormat("#,##0", symbols);
    }

    @Override
    public @NotNull String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public @NotNull String getAuthor() {
        List<String> authors = plugin.getDescription().getAuthors();
        return authors.isEmpty() ? "xUnLuck" : String.join(", ", authors);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return ZERO;
        }

        User user = essentials.getUser(player.getUniqueId());
        if (user == null) {
            return ZERO;
        }

        BigDecimal balance = user.getMoney();

        switch (params.toLowerCase(Locale.ROOT)) {
            case "formatted":
                return wholeNumberFormat.format(balance);

            case "formatted_decimals":
                return decimalFormat.format(balance);

            case "short":
                return formatShort(balance);

            default:
                return wholeNumberFormat.format(balance);
        }
    }

    private String formatShort(BigDecimal balance) {
        double value = balance.doubleValue();

        if (value >= TRILLION) {
            return compact(value, TRILLION, "T");
        }
        if (value >= BILLION) {
            return compact(value, BILLION, "B");
        }
        if (value >= MILLION) {
            return compact(value, MILLION, "M");
        }
        if (value >= THOUSAND) {
            return compact(value, THOUSAND, "K");
        }

        return wholeNumberFormat.format(balance);
    }

    private String compact(double value, double divider, String suffix) {
        return String.format(Locale.US, "%.1f%s", value / divider, suffix);
    }
}
